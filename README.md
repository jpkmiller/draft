# mini-pipeline

## Development

```shell
./mvnw compile quarkus:dev
```

or 

```shell script
./mvnw package
```

## Implementations

### [Implementation 1](impl1And2/src/main/java/com/example/impl1)

#### Pro

- really easy to implement and maintain
- easy to extend and add more stages since `Source`, `Stage` and `Sink` use the same interface

#### Cons

- only serial execution
    - no branches possible
        - no parallel steps possible
        - no multiple sinks possible
        - no multiple sources possible


### [Implementation 2](impl1And2/src/main/java/com/example/impl2)

#### Pro

- serial AND parallel execution
    - solves problems of impl1
- easy to extend
-

#### Cons

-


### [Implementation 3](impl3)
