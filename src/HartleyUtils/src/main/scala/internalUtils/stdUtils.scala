package internalUtils

import internalUtils.Reporter._;

/*
 * Contained below are various misc shortcut methods.
 * 
 * Author - Stephen Hartley
 */

object stdUtils {

  /*
   * Utility Classes:
   */
 
  /**************************************************************************************************************************
   * timestamp / memory-usage utilities
   **************************************************************************************************************************/
  
  def standardStatusReport(initialTimeStamp : TimeStampUtil) {
    val currTime = TimeStampUtil();
    reportln("[Time:			" + currTime.toString + "]","note");
    reportln("[Mem usage:		" + MemoryUtil.memInfo +"]","note");
    reportln("[Elapsed Time:	" + TimeStampUtil.timeDifferenceFormatter(currTime.compareTo(initialTimeStamp)) +"]","note");
  }
  
  val STANDARD_DATE_AND_TIME_FORMATTER = (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
  
  def getDateAndTimeString : String = {
    return STANDARD_DATE_AND_TIME_FORMATTER.format(java.util.Calendar.getInstance().getTime());
  }
   
  object TimeStampUtil {
    def apply() : TimeStampUtil = new TimeStampUtil(java.util.Calendar.getInstance.getTimeInMillis());
    
    def timeDifferenceFormatter(time : Long) : String = {
      val millis_col = zeroPad( (time % 1000).toInt , 4);
      val secs = time / 1000;
      val secs_col = zeroPad( (secs % 60).toInt , 2);
      val mins = secs / 60;
      val mins_col = zeroPad( (mins % 60).toInt , 2);
      val hours = mins / 60;
      val hours_col = zeroPad( (hours % 24).toInt , 2);
      val days = hours / 24;
      
      if(days > 0) return ""+days+" days "+hours_col+":"+mins_col+":"+secs_col+"."+millis_col;
      else ""+hours_col+":"+mins_col+":"+secs_col+"."+millis_col;
    }
  }
  case class TimeStampUtil(ts : Long){
    def compareTo(b : TimeStampUtil) : Long = this.ts - b.ts;
    override def toString : String = {
      val date = new java.util.Date(ts);
      STANDARD_DATE_AND_TIME_FORMATTER.format(date);
    }
  }
  
  object MemoryUtil {
    def totalMem : Long = {
      return Runtime.getRuntime().totalMemory();
    }
    def freeMem : Long = {
      return Runtime.getRuntime().freeMemory();
    }
    def usedMem : Long = {
      return totalMem - freeMem;
    }
    
    def memInfo : String = {
      val numFormatter : java.text.NumberFormat = java.text.NumberFormat.getInstance();
      return "[" + formatByteCount(usedMem) + " / " + formatByteCount(totalMem) + "]";
    }
  }
  
  /**************************************************************************************************************************
   * Sequence/iterator utilities:
   **************************************************************************************************************************/
  
  //Natural Number Iterator:
  def getNaturalNumberIterator(start : Int) : Iterator[Int] = {
    return new Iterator[Int](){
      var curr = start - 1;
      def hasNext : Boolean = true;
      def next : Int = {
        curr += 1;
        return curr;
      }
    }
  }  
   
  //zip an iterator with a count.
  def zipIteratorWithCount[T](iter : Iterator[T], start : Int = 0) : Iterator[(T,Int)] = {
    iter.zip(getNaturalNumberIterator(start));
  }
  
  def repToSeq[T](toCopy : T, times : Int): Seq[T] = {
    if(times == 0) return Seq()
    else if(times == 1) return Seq(toCopy);
    else {
      return ((0 until times).map((x) => toCopy) ).toSeq;
    }
  }
  
  def wrapIteratorWithCutoff[B](iter : Iterator[B], cutoff : Int) : Iterator[B] = {
    new Iterator[B] {
      var iterCt = 0;
      def hasNext : Boolean = iterCt < cutoff && iter.hasNext;
      def next : B = {
        iterCt += 1;
        return iter.next;
      }
    }
  }
  
  def peekIterator[B](iter : Iterator[B], peekCt : Int) : (Vector[B],Iterator[B]) = {
    var soFar : Vector[B] = Vector[B]();
    for(i <- Range(0,peekCt)){
      if(iter.hasNext){
        soFar = soFar :+ iter.next;
      } else {
        return (soFar, soFar.iterator);
      }
    }
    return (soFar, soFar.iterator ++ iter);
  }
  
  /**************************************************************************************************************************
   * Iterator progress reporting
   **************************************************************************************************************************/
  
  abstract class IteratorProgressReporter {
    def reportProgress(iterCt : Int);
    
    def wrapIterator[B](iter : Iterator[B]) : Iterator[B] = {
      val ips = this;
      new Iterator[B]{
        var iterCt = 0;
        def hasNext : Boolean = iter.hasNext;
        def next : B = {
          iterCt += 1;
          ips.reportProgress(iterCt);
          return iter.next;
        }
      }
    }
  }
  case class IteratorProgressReporter_ThreeLevel(elementTitle : String, dotThreshold : Int, dotSpaceThreshold : Int , dotNewlineThreshold : Int ) extends IteratorProgressReporter  {
    def reportProgress(iterCt : Int){
      if(iterCt % dotThreshold == 0){ if(iterCt % dotSpaceThreshold == 0) { if(iterCt % dotNewlineThreshold == 0) report(".["+iterCt+" "+elementTitle+" processed] [Time: "+getDateAndTimeString+"] \n","progress"); else report(". ","progress"); } else { report(".","progress");}}
    }
  }
  
  def wrapIteratorWithProgressReporter[B]( iter : Iterator[B] , ipr : IteratorProgressReporter ) : Iterator[B] = {
    new Iterator[B]{
      var iterCt = 0;
      def hasNext : Boolean = iter.hasNext;
      def next : B = {
        iterCt += 1;
        ipr.reportProgress(iterCt);
        return iter.next;
      }
    }
  }
  
  object presetProgressReporters {
    val DEFAULT_ITERATOR_PROGRESS_REPORTER_READPAIRS : IteratorProgressReporter = IteratorProgressReporter_ThreeLevel("Read-Pairs", 100000, 1000000, 1000000);
    
    def wrapIterator_readPairs[B](iter : Iterator[B], verbose : Boolean = true, cutoff : Int = -1) : Iterator[B] = {
      if((! verbose) && (cutoff == -1)) return iter;
      else if((  verbose) && (cutoff == -1)) return DEFAULT_ITERATOR_PROGRESS_REPORTER_READPAIRS.wrapIterator(iter);
      else if((  verbose) && (cutoff != -1)) return wrapIteratorWithCutoff(DEFAULT_ITERATOR_PROGRESS_REPORTER_READPAIRS.wrapIterator(iter), cutoff);
      else return wrapIteratorWithCutoff(iter, cutoff);
    }
  }
  
  /**************************************************************************************************************************
   * String conversion:
   **************************************************************************************************************************/
  
  def string2float(s : String) : Float = {
    augmentString(s).toFloat
  }
  def string2int(s : String) : Int = {
    augmentString(s).toInt
  }
  def string2long(s : String) : Long = {
    augmentString(s).toLong
  } 
  def string2double(s : String) : Double = {
    augmentString(s).toDouble
  }
  
  /**************************************************************************************************************************
   * String formatting
   **************************************************************************************************************************/
  def cleanQuotes(s : String) : String = {
    if(s.length < 2) s;
    else if(s.charAt(0) == '"' && s.charAt(s.length - 1) == '"'){
      s.substring(1,s.length - 1)
    } else {
      s
    }
  } 
  
  def formatByteCount(b : Long) : String = {
    if(b < 5000L) b.toString + "B";
    else if(b < 5000000L) (b / 1000L).toString + "kB";
    else if(b < 5000000000L) (b / 1000000L).toString + "MB";
    else if(b < 5000000000000L) (b / 1000000000L).toString + "GB";
    else (b / 5000000000000L).toString + "TB";
  }
   
  def zeroPad(i : Int, cols : Int) : String = {
    val s = i.toString;
    return 0.toString * (cols - s.length) + s;
  }
  
  def padString(s : String, cols : Int, truncate : Boolean = false, padChar : String = " ", alignment : String = "right") : String = {
    if((! truncate) && s.length() > cols){
      error("ERROR! String too long for given column, and no truncation allowed: col="+cols+", string=\"" + s + "\"!");
    }
    if(alignment == "right"){
      padChar * (cols - s.length) + s;
    } else if(alignment == "left"){
      s + padChar * (cols - s.length);
    } else {
      error("ERROR! String Justification Method Not Recongized: \"" + alignment + "\"!");
      return null;
    }
  }

  
  /**************************************************************************************************************************
   * Line formatting:
   **************************************************************************************************************************/
  def lineseq2string(ss : Seq[String]) : String = {
    //ss.foldLeft("")(_ +"\n"+ _) + "\n";
    ss.mkString("\n");
  }
  private val whiteSpaceString = "                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          ";
  private val maxWrapWidth = whiteSpaceString.length;
  
  def wrapLineWithIndent(line : String, width : Int, indent : Int) : String = {
    wrapLinesWithIndent(line, width, whiteSpaceString.substring(0,indent), false);
  } 
  
  def wrapLinesWithIndent(lines : String, width : Int, indent : String, removeExistingWrapBreaks : Boolean) : String = {
    val linesSeq : Seq[String] = lines.split("\n");
    return lineseq2string(wrapLinesWithIndent(linesSeq,width, indent, removeExistingWrapBreaks));
  }
  
  def wrapLinesWithIndent(lines : Seq[String], width : Int, indent : String, removeExistingWrapBreaks : Boolean) : Seq[String] = {
    if(removeExistingWrapBreaks){
      val paras : Seq[String] = lines.foldLeft(Vector[String]())((soFar,line) => {
        if(line == ""){
          soFar :+ ""
        } else {
          soFar.init :+ (soFar.last + line);
        }
      });
      wrapLinesWithIndent_helper(paras, width, indent);
    } else {
      wrapLinesWithIndent_helper(lines , width , indent);
    }
  }
   
  def wrapLinesWithIndent_helper(paras : Seq[String], width : Int, indent : String) : Seq[String] = {
    paras.foldLeft(Vector[String]())( (soFar, curr) =>{
      soFar ++ wrapLinesWithIndent_tailRecursive(Vector[String](), curr, width, indent);
    })
  }
  
  private def wrapLinesWithIndent_tailRecursive_DEPRECIATED(acc : Seq[String], para : String, width : Int, indent : String) :  Seq[String] = {
    if(para.length + indent.length <= width) acc :+ (indent + para);
    else  {
      wrapLinesWithIndent_tailRecursive( acc :+ (indent + para.substring(0,width)), para.substring(width), width , indent);
    }
  }
  
  private def wrapLinesWithIndent_tailRecursive(acc : Seq[String], para : String, width : Int, indent : String) :  Seq[String] = {
    if(para.length + indent.length <= width) acc :+ (indent + para);
    else  {
      val (nextLine, paraRemainder) = wrapLinesWithIndent_tailRecursiveHelper_buildLine(indent, para, width, indent.length);
      return wrapLinesWithIndent_tailRecursive(acc :+ nextLine, paraRemainder, width, indent);
    }
  }
  
  private def wrapLinesWithIndent_tailRecursiveHelper_buildLine(lineSoFar : String, untrimmedPara : String, width : Int, indentLen : Int) : (String,String) = {
    val para = untrimmedPara.trim
    if(para.length == 0) return (lineSoFar,para);
    
    val indexOfNextSpace = para.indexOf(' ');
    val wordLen = if(indexOfNextSpace == -1) para.length; else indexOfNextSpace;
    
    if(indentLen + wordLen > width) return (lineSoFar + para.substring(0, width - lineSoFar.length - 1) + "-", para.substring(width - lineSoFar.length - 1));
    if(lineSoFar.length + wordLen > width) return (lineSoFar,para);

    val word = para.substring(0,wordLen);
    val paraRemainder = para.substring(wordLen);
    return( wrapLinesWithIndent_tailRecursiveHelper_buildLine(lineSoFar + word + " ", paraRemainder, width, indentLen) );
  }
  
  /**************************************************************************************************************************
   * Parallelization:
   **************************************************************************************************************************/
  
  def parConvert[T](v : Seq[T], numThreads : Int) : scala.collection.GenSeq[T] = {
    if(numThreads == 1) {
      return v;
    } else {
      val vpar = v.par;
      vpar.tasksupport = new scala.collection.parallel.ForkJoinTaskSupport(new scala.concurrent.forkjoin.ForkJoinPool(numThreads));
      vpar;
    }
  } 
  



}
















