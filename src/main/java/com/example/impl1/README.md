# Implementation 1

## Pro

- really easy to implement and maintain
- easy to extend and add more stages since `Source`, `Stage` and `Sink` use the same interface

## Cons

- serial execution
  - no branches possible
    - no parallel steps possible
    - no multiple sinks possible
    - no multiple sources possible
