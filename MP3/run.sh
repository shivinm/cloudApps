mvn clean package;
storm jar target/storm-example-0.0.1-SNAPSHOT.jar TopWordFinderTopologyPartA > output-part-a.txt;
storm jar target/storm-example-0.0.1-SNAPSHOT.jar TopWordFinderTopologyPartB data.txt > output-part-b.txt;
storm jar target/storm-example-0.0.1-SNAPSHOT.jar TopWordFinderTopologyPartC data.txt > output-part-c.txt;
storm jar target/storm-example-0.0.1-SNAPSHOT.jar TopWordFinderTopologyPartD data.txt > output-part-d.txt;