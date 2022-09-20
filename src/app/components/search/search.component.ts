import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Homeinsurance } from 'src/app/models/homeinsurance';
import { HomeinsuranceService } from 'src/app/services/homeinsurance.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
homeinsurances:Homeinsurance[]=[]
  constructor(private _homeinsuranceService:HomeinsuranceService,
    private _router:Router,
    private _activatedRoute:ActivatedRoute) { }

  ngOnInit(): void {
    
  }
  show = (propertyName:string,choice: string) => {
    this._router.navigate(["/homeinsurance",propertyName,choice]);
  }
//   calculate=(cost:string ,sqrFeet:string)=>{
   
//     this._router.navigate(["/homeinsurance",cost,sqrFeet]);
//   }


 }
