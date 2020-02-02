- monads
- functors
- formally defined
- extremely general
- generality means they are notoriously difficult to understand
- everyone finds abstractions difficult

`typeclass patter`: type type class, instances for particular types, onterface methods which users can use

- a type class is an inetrface or api that represents some functionality we want to implement

`extension methods`:

# wix lecture notes

- 'the expression problem'
- type class components
  - the signature
  - the Implementations for supported types
  - a function the requires a type class
- 'implicits' are confusing
  - the last parameter list in a scala function can be marked as implicit
  - implicit parameters are filled in by the compiler
  - you can specify an implicit explicitly
  - they are used to require evidence of the compiler
  - like the existance of a type class in scope
- type safe equality
- use implicit not found annotation
- think more about annotations...
- type ascription
- \_\* is a special instance of type ascryption

# cats docs notes

- parametric polymorphism
- type classes enable ad hoc polymorphis
- polymorphism
- overloading
- monoid?
- subtype constraints

# wikipedia notes

`type class:` a type class is a type sytem construct that supports parametric polymorphism.

`type class:` a type class is an interface that defines some behavior. More specifically, a type class specifies a bunch of functions, and when we decide to make a type an instance of a type class, we define what those functions mean for that type.

`type class:` a type class is a programming technique that lets you add new behavior to closed data types without using inheritence, and without having to access the original sorce code of those types.

`parametric polymorphism:` in programming languages and type theory parametric polymorphism is a way to make a language more expressive, whilst still maintaining full static type safety. Using parametic polymorphism, a function or datatype can be written generically such that it can handle values identically without depending on their type. Such functions and datatypes are called generic functions and generic datatypes, and form the basis of generic programming

`polymorphism:` polymorphism is the provision of asingle interface, to entities of different types, or the use of a single symbol to represent multiple types. there are multiple classes of polymorphism, namely `ad hoc polymorphism`, `parametric polymophism` and `subtyping`

`ad hoc polymorphism:` ad hoc polymorphism is a kind of polymorphism in which polymorphic functions can be applied to arguments of different types, because polymorphic functions can denote a number of distinct and potentially heterogeneous implementations depending on the type of arguments to which it is applied. it is also known as funtion overloading or operator overlaoding.

`interface:` an interface is a shared boundry accross which two or more separate components of a computer system exchange information

`data type:` a data type is an attribute of data which tells the compiler or interpreter, how the programmer intends to use the data

`data:` individual units of information

`type theory:` atype theory is any class of formal systems. in type theory every term has a type and operations are restricted to terms of a certain type

`formal system:` A formal system is used for inferring theorems from axioms according to a set of rules. these rules, which are used for carrying out the inference of theorems and axioms, are the logical calculus of the formal system. a formall system is essentially an axiomatic system.

`axiom:` an axiom or postulate is a statement which is taken to be true, to serve as a premise or starting point for furthur reasoning and arguments. the word comes from the greek `ἀξίωμα` 'that which commends it self as evident

`axiomatic system:` ana xiomatoc system is any set pf acioms from which some or all axioms can be used in conjunction to logically derive theorems

`domain driven design (DDD):` an approach to software development for complex needs by connecting the implementation to an evolving mode, predecated on the followin goals:

- placing the projects primary focus on the cor odmain and domain logic
- basing complex designs ona amodel of the domain
- initiating a creative collaboration between technical and domain expers to iteratively refine a conceptual model that addresses a particular domain of problems.

`refinement types:` in type theory a refinement type is a type endowed with a predicate which is assumed to hold for any element of the refined type.

`singleton pattern:` a software design pattern that restricts the instantiation of a class to one single instance

`class:` a class is an extensible program code template for creating objects, providing initial values for state and implementations of behavior

`kind:` in type theory a kind is the type of a type constructor, or less commonly, the type of a higher order type operator.

`type constructor:` a type constructor is a feature of a typed formal language that builds new types from old ones

`monoid:` a monoid is an algebreic structure with a single associative, binary operation and identity element

`monad:` a monad is a design pattern, that allows structuring programs generically while automating away boilerplate code needed by the programming logic. Monads achive this by providing their own data type which represents a particular form of computation, along with one procedure to wrap values of any basic type with the monad, yielding a monadic value and another to compose functions that output monadic values called monadic funcitonss

## scala specific notes

# what the hell is a type class??

> https://typelevel.org/cats/typeclasses.html

> http://tpolecat.github.io/2015/04/29/f-bounds.html

> https://docs.scala-lang.org/glossary/index.html

> https://en.wikipedia.org/wiki/Functional_programming

> https://www.scala-lang.org/blog/2016/12/07/implicit-function-types.html#implicit-functions

> https://docs.scala-lang.org/overviews/scala-book/functional-error-handling.html

### alvin alexanders notes, particually brilliant

> https://alvinalexander.com/scala/fp-book/type-classes-101-introduction

## scala specific notes

# underscore notes

```sh
printf 'hello my name is alex' | tr ' ' '\n' >> .gitignore
```

- eq is used for type safe equality in scala
- could be important when unit testing in scala check

`variance:` variance relates to subtypes

`(co / contra) variance:` these annotations arise when working with type constructors

- chapter 1 covers invariance, covariance and contravariance

- `monoid:` a monoid for type A is

  - an operation combine with type (A,A) => A
  - an element empty of type A

- `semigroup:` a semigroup for type A is

  - an operation combine with type (A,A) => A

- NB the cats kernal is a subproject of cats, providing a small set of typeclasses for libraris that dont require the full cats toolbox

```scala
|+| is the combine operator
```

- big data applications like spark and hadoop distribute data analysis over man machines, giving fault tollerant scalabiltiy. implicing that each machine will return results over a portion of the data

- `eventual concistency:`
