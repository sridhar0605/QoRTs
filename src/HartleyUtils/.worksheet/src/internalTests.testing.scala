package internalTests

import net.sf.samtools._


object testing {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(110); 
  println("Welcome to the Scala worksheet");$skip(37); 
  
  val t = CigarOperator.INSERTION;System.out.println("""t  : net.sf.samtools.CigarOperator = """ + $show(t ));$skip(55); 
  val t2 = CigarOperator.SOFT_CLIP.consumesReadBases();System.out.println("""t2  : Boolean = """ + $show(t2 ));$skip(60); 
  val t3 = CigarOperator.SOFT_CLIP.consumesReferenceBases();System.out.println("""t3  : Boolean = """ + $show(t3 ));$skip(24); 

  val t4 = Range(0,10);System.out.println("""t4  : scala.collection.immutable.Range = """ + $show(t4 ));$skip(33); 

  val i1 = Iterator[Int](1,2,3);System.out.println("""i1  : Iterator[Int] = """ + $show(i1 ));$skip(21); 

  val i11 = i1.next


  class TestClass() {
    val myArray = Array[Int](0,1,2,3,4,5);
    def apply(i : Int) : Int = {
      return myArray(i);
    }
    def update(i : Int, j : Int){
      myArray(i) = j;
    }
  };System.out.println("""i11  : Int = """ + $show(i11 ));$skip(228); 
  
  val tc = new TestClass();System.out.println("""tc  : internalTests.testing.TestClass = """ + $show(tc ));$skip(23); ;
  
  val tct1 = tc(1);System.out.println("""tct1  : Int = """ + $show(tct1 ));$skip(18); ;
val tct3 = tc(2);System.out.println("""tct3  : Int = """ + $show(tct3 ));$skip(14); ;
  tc(2) += 1;$skip(21); ;
  
val tct2 = tc(2);System.out.println("""tct2  : Int = """ + $show(tct2 ));$skip(34); ;

 val str = "%1.3f".format(115.0);System.out.println("""str  : String = """ + $show(str ));$skip(97); 
 
 val tttsts = Seq('A'.toInt ,'B'.toInt, 'C'.toInt,'G'.toInt,'N'.toInt,'S'.toInt,'H'.toInt).max;System.out.println("""tttsts  : Int = """ + $show(tttsts ))}
 
 
}
