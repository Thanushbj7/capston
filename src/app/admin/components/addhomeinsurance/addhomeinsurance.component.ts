import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Homeinsurance } from 'src/app/models/homeinsurance';
import { HomeinsuranceService } from 'src/app/services/homeinsurance.service';

@Component({
  selector: 'app-addhomeinsurance',
  templateUrl: './addhomeinsurance.component.html',
  styleUrls: ['./addhomeinsurance.component.scss']
})
export class AddhomeinsuranceComponent implements OnInit {

  homeinsurance:Homeinsurance={
     insuranceId:0,
         name:'',
         sumAssured:0,
          coverage:'',
         term:0,
         premium:0,
        //  vendor:{
        //   vendorId:0,
        //   name:'',
        //   address:{
        //     addressId:0,
        //     city:'',
        //     state:'',
        //     zipcode:0

        //   }
        //  }
  }
  
  constructor(private _homeinsuranceService:HomeinsuranceService) { }

  ngOnInit(): void {
  }
  onSubmit=(addForm:NgForm)=>{
    console.log(addForm.value);
    console.log(this.homeinsurance);
    this._homeinsuranceService.addInsurance(this.homeinsurance).subscribe({
      next:(data)=>{
        console.log(data);
      }

    })
  }
}
