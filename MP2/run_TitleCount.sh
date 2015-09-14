hadoop fs -rm -r -f /mp2/A-output
rm -rf ./build ./TitleCount.jar ./output;
mkdir build; mkdir output;
export HADOOP_CLASSPATH=$JAVA_HOME/lib/tools.jar;
hadoop com.sun.tools.javac.Main TitleCount.java -d build;
jar -cvf TitleCount.jar -C build/ ./;
hadoop jar TitleCount.jar TitleCount -D stopwords=/mp2/misc/stopwords.txt -D delimiters=/mp2/misc/delimiters.txt /mp2/titles /mp2/A-output;
hadoop fs -get /mp2/A-output/part* ./output;
