import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Homeinsurance } from 'src/app/models/homeinsurance';
import { HomeinsuranceService } from 'src/app/services/homeinsurance.service';

@Component({
  selector: 'app-homeinsurance-details',
  templateUrl: './homeinsurance-details.component.html',
  styleUrls: ['./homeinsurance-details.component.scss']
})
export class HomeinsuranceDetailsComponent implements OnInit {
  insuranceId:number=0;
  homeinsurance!:Homeinsurance;
  constructor(private _router:Router,
    private _activatedRoute:ActivatedRoute,
    private _homeinsuranceService:HomeinsuranceService) { }

  ngOnInit(): void {
    this._activatedRoute.paramMap.subscribe((map)=>{
      // pass the key
      let pid = map.get("id");
      if(pid)
        this.insuranceId = parseInt(pid);
    });
    
    this._homeinsuranceService.getById(this.insuranceId).subscribe({
      next:(data)=>{
        this.homeinsurance =data;
      }
    })
  }
  }


