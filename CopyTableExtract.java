
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
