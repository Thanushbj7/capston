
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
