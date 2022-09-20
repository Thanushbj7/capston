import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Homeinsurance } from 'src/app/models/homeinsurance';
import { HomeinsuranceService } from 'src/app/services/homeinsurance.service';

@Component({
  selector: 'app-homeinsurance',
  templateUrl: './homeinsurance.component.html',
  styleUrls: ['./homeinsurance.component.scss']
})
export class HomeinsuranceComponent implements OnInit {
homeinsurances:Homeinsurance[]=[]
  constructor(private _homeinsuranceService:HomeinsuranceService,
              private _router:Router,
              private _activatedRoute:ActivatedRoute) { }

  ngOnInit(): void {
   
    this._homeinsuranceService.getHomeInsurance().subscribe({
      next:(data)=>{
        console.log(`${data}`);
        this.homeinsurances = data;
      },
      
    });
    
    
  }
  show =(insuranceId:number)=>{
   
    // call navigate method to move to another route
    // the url is like /investment-details/1 inthe browser
    // the route path  is like /investment-details/:id in the browser
    this._router.navigate(["/homeinsurance-details",insuranceId]);

  }
}
