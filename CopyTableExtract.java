
@wire(getObject) wiredCases(value){
    this.wiredRecords = value;
    const { data, error } = value;

    if(data){
        let tempRecords = JSON.parse(JSON.stringify(data));

        tempRecords = tempRecords.map(row => {
            return {
                ...row,
                CaseNumber: row.Case__r.CaseNumber,
                CreatedDate: row.Case__r.CreatedDate,
                [row.Call_Activity__c]: row.Call_Type__c // Assigning Call_Type__c to its respective Call_Activity__c column
            };
        });

        this.data = tempRecords;
        console.log("tempRecords!", tempRecords);
    }

    if(error){
        console.log("error Occurred!", error);
    }
}





@wire(getObject) wiredCases(value){
    this.wiredRecords = value;
    const { data, error } = value;

    if(data){
        let tempRecords = JSON.parse(JSON.stringify(data));
        let activityMap = {};

        tempRecords.forEach(row => {
            const activity = row.Call_Activity__c;
            if (!activityMap[activity]) {
                activityMap[activity] = [];
            }

            activityMap[activity].push({ ...row, CaseNumber: row.Case__r.CaseNumber, CreatedDate: row.Case__r.CreatedDate });
        });

        // Flatten the map values into a single array
        this.data = Object.values(activityMap).flat();
        console.log("tempRecords!", this.data);
    }

    if(error){
        console.log("error Occurred!", error);
    }
}





<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-resources-plugin</artifactId>
            <version>2.6</version>
        </plugin>
        <!-- Other plugins may follow here -->
    </plugins>
</build>



    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.voya</groupId>
  <artifactId>CopyTableAutomation</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  
</project>



    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.voya</groupId>
  <artifactId>CopyTableAutomation</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  
</project>
