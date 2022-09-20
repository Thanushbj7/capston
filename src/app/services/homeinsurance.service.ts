import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Homeinsurance } from '../models/homeinsurance';

@Injectable({
  providedIn: 'root'
})
export class HomeinsuranceService {
  baseurl = "http://localhost:8080/homeinsurance-api/homeinsurance"
  constructor(private _http:HttpClient) { }
  addInsurance=(homeinsurance:Homeinsurance)=>{
    return this._http.post<Homeinsurance[]>(this.baseurl,homeinsurance);
   }
   getHomeInsurance =():Observable<Homeinsurance[]>=>{
    return this._http.get<Homeinsurance[]>(this.baseurl);

}
getById =(newplanId:number):Observable<Homeinsurance>=>{
  let url = this.baseurl.concat("/id/")+newplanId;
  return this._http.get<Homeinsurance>(url);
}
getByCostAndSqrFeet =(cost:number,sqrfgeet:number):Observable<Homeinsurance[]>=>{
  let url = this.baseurl.concat("/cost/")+cost+"/squareft/"+"sqrfgeet";
  return this._http.get<Homeinsurance[]>(url);
}

getByTerm =(term:number):Observable<Homeinsurance[]>=>{
  let url = this.baseurl.concat("/term/")+term;
  return this._http.get<Homeinsurance[]>(url);
}

getByCoverage =(coverage:string):Observable<Homeinsurance[]>=>{
  let url = this.baseurl.concat("/coverage/")+coverage;
  return this._http.get<Homeinsurance[]>(url);
}

getByPremium =(premium:number):Observable<Homeinsurance[]>=>{
  let url = this.baseurl.concat("/premium/")+premium;
  return this._http.get<Homeinsurance[]>(url);
}

}