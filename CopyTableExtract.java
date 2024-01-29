<template>
		<div style="height: 300px;">
				<h2 slot="title">
            <lightning-icon icon-name="action:new_note"  title=" Case History"></lightning-icon>
           <b>Case History</b>
        </h2>
		
		<lightning-datatable
        data={data}
        columns={columns}
        key-field="Id"
			  show-row-number-column
			  row-number-offset={rowOffset}
			  hide-checkbox-column       
    >
    </lightning-datatable>
		</div>
		 
</template>





				  <div style="height: 300px;">
    <h2>
        <span>
            <img src="path/to/your/icon.png" alt="Case History Icon" width="24" height="24">
        </span>
        <b>Case History</b>
    </h2>

    <table>
        <thead>
            <tr>
                <th>Case Number</th>
                <th>Date</th>
                <th>Plan Id</th>
                <th>Inquiry</th>
                <th>Transactions</th>
                <th>Account Maintenance</th>
                <th>Forms</th>
                <th>Others</th>
            </tr>
        </thead>
        <tbody>
            <!-- Loop through data to generate rows -->
            <template for:each={data} for:item="record" for:index="index">
                <tr key={record.Id}>
                    <td>{record.CaseNumber}</td>
                    <td>{record.CreatedDate}</td>
                    <td>{record.PlanID_Text__c}</td>
                    <td>{record.Inquiry}</td>
                    <td>{record.Transactions}</td>
                    <td>{record.AccountMaintenance}</td>
                    <td>{record.Forms}</td>
                    <td>{record.Others}</td>
                </tr>
            </template>
        </tbody>
    </table>
</div>







				  <template>
    <div style="height: 300px;">
        <h2>
            <span>
                <img src="path/to/your/icon.png" alt="Case History Icon" width="24" height="24">
            </span>
            <b>Case History</b>
        </h2>

        <table>
            <thead>
                <tr>
                    <th>Case Number</th>
                    <th>Date</th>
                    <th>Plan Id</th>
                    <th>Inquiry</th>
                    <th>Transactions</th>
                    <th>Account Maintenance</th>
                    <th>Forms</th>
                    <th>Others</th>
                </tr>
            </thead>
            <tbody>
                <!-- Loop through data to generate rows -->
                <!-- Assuming each record is an object with properties like CaseNumber, CreatedDate, etc. -->
                <template for:each={data} for:item="record" for:index="index">
                    <tr key={record.Id}>
                        <td>{record.CaseNumber}</td>
                        <td>{record.CreatedDate}</td>
                        <td>{record.PlanID_Text__c}</td>
                        <td>{record.Inquiry}</td>
                        <td>{record.Transactions}</td>
                        <td>{record.AccountMaintenance}</td>
                        <td>{record.Forms}</td>
                        <td>{record.Others}</td>
                    </tr>
                </template>
            </tbody>
        </table>
    </div>
</template>



<template>
    <div class="slds-box slds-theme_default" style="height: 300px;">
        <header class="slds-box_header slds-p-around_small slds-theme_default">
            <h2 class="slds-text-heading_medium">
                <lightning-icon icon-name="action:new_note" title="Case History" size="medium"></lightning-icon>
                <b>Case History</b>
            </h2>
        </header>

        <div class="slds-scrollable_x">
            <lightning-datatable
                key-field="Id"
                data={data}
                columns={columns}
                hide-checkbox-column
            ></lightning-datatable>
        </div>
    </div>
</template>
				  
