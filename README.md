[![Build Status](https://travis-ci.org/Peggir/SimpleDbUtil.svg?branch=master)](https://travis-ci.org/Peggir/SimpleDbUtil)
[![Maintainability](https://api.codeclimate.com/v1/badges/fee6c8e874238496311f/maintainability)](https://codeclimate.com/github/Peggir/SimpleDbUtil/maintainability)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.peggir/SimpleDbUtil/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.peggir/SimpleDbUtil)

# SimpleDbUtil - For Play! Framework
Provides an easy to use interface to insert or retrieve data to/from your database from your Play! Framework application. It actually is syntactic sugar over Java's Connection class. It support parameterized queries for save data insertion, object mapping and more.

## Features
* Execute (parameterized) SQL-queries
* Map database result sets to objects
* Insert data in the database
* Retrieve data from the database
* Check if your query yields any result

## Requirements
* Play! Framework 2.6+
* Java 8

## Installation
Add SimpleDbUtil to your project build tool dependencies:
```
<dependency>
    <groupId>com.peggir</groupId>
    <artifactId>SimpleDbUtil</artifactId>
    <version>{VERSION}</version>
</dependency>
```
### Versions
| SimpleDbUtil  | Play          | Scala target  |
| ------------- |:-------------:| -------------:|
| 1.2.0         | 2.8.1         | 2.13          |
| 1.1.0         | 2.7.4         | 2.13          |
| 1.0.1         | 2.6.6         | 2.12          |

## Usage examples
In the following examples we have a database with the following table:
```SQL
CREATE TABLE "person" (
  "id"             SERIAL      NOT NULL,
  "name"           VARCHAR(30) NOT NULL,
  "age"            INTEGER     NOT NULL,
  "isStudent"      BOOLEAN     NOT NULL,
  "score"          FLOAT       NULL,
  "submissionDate" TIMESTAMP   NULL,
  CONSTRAINT pk_person PRIMARY KEY ("id")
);
```
And we have a model representing our `person` table:
```java
public class Person {
    
    private int id;
    private String name;
    private int age;
    private boolean isStudent;
    private float score;
    private Date submissionDate;

    public Person(int id, 
                  String name, 
                  int age, 
                  boolean isStudent, 
                  float score, 
                  Date submissionDate) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.isStudent = isStudent;
        this.score = score;
        this.submissionDate = submissionDate;
    }
    
    // Imagine getters and setters here
    
}

```
Also note that the `database` object in the code examples are `play.db.Database` objects that can be injected into your play controllers.

### Retrieve data from the database
#### hasResults
Returns whether your query has one or more results. The following example uses a query with two parameters (notice the question marks). `name` and `age` get safely injected into the statement (to prevent SQL-injection). If your query has no parameters, then the third parameter of the `DbCall`-constructor can be removed.
```java
public boolean existsPerson(String name, int age) throws DbCallException {
    return new DbCall<>(
            database,
            "SELECT 1 FROM \"person\" WHERE \"name\"=? AND \"age\"=?;",
            stmt -> {
                stmt.setString(1, name);
                stmt.setInt(2, age);
            }
    ).hasResults();
}
```

#### getOne
Returns the mapped representation of the first result of your SQL-query. Here you need a `DbCallResultSetMapper` to map the result set to an object. SimpleDbUtil already provides to following result set mappers: `BooleanResultSetMapper`, `DateResultSetMapper`, `FloatResultSetMapper`, `IntegerResultSetMapper`, `LongResultSetMapper` and `StringResultSetMapper`. You can create your own by implementing `DbCallResultSetMapper` (see example for `getAll`). The following DbCall returns the age of the person with a given name.
```java
public int getAgeByName(String name) throws Exception {
    Optional<Integer> age = new DbCall<>(
            database,
            new IntegerResultSetMapper(),
            "SELECT 1 FROM \"person\" WHERE \"name\"=?;",
            stmt -> stmt.setString(1, name)
    ).getOne();
    
    if (!age.isPresent()) {
        throw new Exception("Person with this name does not exist");
    }
    
    return age.get();
}
```

#### getAll
Returns a list of all mapped objects representing the entire result set of your query. If your query has zero results then an empty list is returned. For the following example we first create a `PersonResultSetMapper` that maps the result set to a `Person`-object. You can also choose to do this in a lambda, but implementing it in a separate class gives you the advantage to reuse your result set mapper.

The result set mapper:
```java
import com.peggir.SimpleDbUtil.DbCallResultSetMapper;
import com.peggir.SimpleDbUtil.exceptions.DbCallResultSetMapperException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonResultSetMapper implements DbCallResultSetMapper<Person> {
    
    @Override
    public Person map(ResultSet rs) throws DbCallResultSetMapperException {
        try {
            return new Person(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getBoolean("isStudent"),
                    rs.getFloat("score"),
                    rs.getTimestamp("submissionDate")
            );
        } catch (SQLException e) {
            throw new DbCallResultSetMapperException(DbCallResultSetMapperException.DEFAULT_ERROR_MSG, e);
        }
    }
    
}
````
Now you can use this result set mapper to get all persons:
```java
public List<Person> getAll() throws DbCallException {
    return new DbCall<>(
            database,
            new PersonResultSetMapper(),
            "SELECT \"id\", \"name\", \"age\", \"isStudent\", \"score\", \"submissionDate\" " +
                    "FROM \"person\" ORDER BY \"submissionDate\";"
    ).getAll();
}
```

### Insert data in the database
#### execute
Simply executes your SQL-query. Use this for database insertion. In the following example we insert a new person.
```java
public void insert(String name,
                   int age,
                   boolean isStudent,
                   float score,
                   Date submission) throws DbCallException {
    new DbCall<>(
            database,
            "INSERT INTO \"person\" (\"name\", \"age\", \"isStudent\", \"score\", \"submissionDate\") VALUES " +
                    " (?,?,?,?,?);",
            stmt -> {
                stmt.setString(1, name);
                stmt.setInt(2, age);
                stmt.setBoolean(3, isStudent);
                stmt.setFloat(4, score);
                stmt.setTimestamp(5, new java.sql.Timestamp(submission.getTime()));
            }
    ).execute();
}
```

## License
Copyright 2020 Peggir

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

<a href="https://peggir.com/"><img src="https://peggir.com/assets/img/peggir/peggir-logo-blue.png" width="200"></a>
