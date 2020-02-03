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

- `functors:` informally, a functor is anything with a map method`

- we need to change the way we think, we should thing of a map as changing all the values in something, in one go

- we need to think of map as a way of sequencing computations on values

- NB futures are not referentially transparent, in adition they do not give us the option to choose when to begin the computation

- `functor:` a functor adheres to the follwoing laws

  - identity
  - functional composition

- in some programs we need to enable higher kinded types

- contramap, represents preapending an operation to a chain

- map generates a new type class instance, by appending a function to a chain

- contramap generates a new type class instance by preapending a function to a chain

- imap generates a a new type class instance by a pair of bidirectional transformers

- informally, a monad is anything with a constructor and a flatmap operation

- `a monad is a mechanism for sequencing computations`

- every monad is also a functor

- Monad

  - pure: A => F[A]
  - flatMap: (F[A], A => F[B]) => F[B]

- monad laws

  - left identity
  - right identity
  - associativity

- either is generally used to implememnt fail-fast error handling.

- cats.eval is a monad that allows us to abstract over different models of evaluation, eg eager, lazy, memoized

- Eager computations happen immediately

- Lazy computations happen on access

- memoized computations are run once on firsed access, and are cached for subsequent calls

- why is Eval useful?

- eval is useful to enforce stack safety, however trampolining is not free. remember

- `trampolining:`

- a common use of the writer monad is recording sequences of steps in multithreaded computations, where standart imperitive logging techniques can result in interleaved messages form different contexts

- dont fully understand the point in the writer monad

- NB log writer example

- dependency injection patterns in scala

- the `khleisli arrow`

- the power of the state monad comes from combining instances
  the map and flatMap methods thread state from one instance to another

- cats provides transformers for many monads, all of which suffixed with T

- EitherT, OptionT etc...

- each monad transformer is a data type defined in cats.data

- things to understand about monad transformers

  - the availible transformer classes
  - how to build stacks of monads using transformers
  - how to construct instances of a monad stack
  - how to pull apart a stack to access the wrapped monads

- ReaderT is a type alias for the kleisli arrow

- kind projector, enhances scalas type syntax to make it easier to define partiallly applied type constuctors (the question mark in your type constuctors)

- pg 153

- `monad transformers` eliminate the need for nested for comprehensions and pattern matching when dealing with stacks of nested monads

- monad transformers are written from the inside out

- Semigroupal

- Applicative

- "functors and monads are useful, however the cannot represent certain types of program flow"

- fail fast monad: Either

- monadic comprehensions only allow us to run taks in sequence

- map and flatmap cant capture what we want because they make the assumption the each computation is dependent of the previous one

- `semigroupal` encompasses the notion of composing pairs of contexts. Cats provides cats.syntax.apply module that makes use of semigroupal and functor to allow users to sequence functions with multiple arguments

- `applicative` extends semigroupal and functor. it provides a way of applying functions to parameters within a context. Applicative is the source of the pure method.

- create then flatmap pattern

- conor mcbride, ross pattern

- "the hierachy of sequencing type classes"

- foldable abstracts foldleft and foldright

- traverse is a high level abstraction that uses applicatives to iterate with less pain than folding

- subtraction is not associative

- doldmap: maps a user supplied function over a sequence and combines the results using a monoid

- combineAll: combines all elements in the sequenc using their monoid

- the traverse type class is a higher level tool that leverages applicatives to provide a more convenient more lawful pattern for iteration

- traverse is one of the msot powerful typeclasses

# list of useful functions

- map
- fold
- lift
- contramap
- imap
- flatmap
- bimap
- mapBoth
- swap
- zip
- product
- ap
- pure
- traverse
- sequence
