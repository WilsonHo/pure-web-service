NOTE

```scala
import eu.timepit.refined.pureconfig._
import doobie.refined.implicits._
import io.circe.refined._
import eu.timepit.refined.auto._

// Remove the implicit error from InteliJ
private implicit def seqFactoryCompat[A]: FactoryCompat[A, List[A]] =
  FactoryCompat.fromFactor(List.iterableFactory)
```
