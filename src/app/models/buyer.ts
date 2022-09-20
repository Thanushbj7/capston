import { Address } from "./address";
import { Homeinsurance } from "./homeinsurance";
import { Property } from "./property";

export class Buyer {
    constructor(

     public planId:number,
     public name:string,
     public moblieNo:number,
     public address:Address,
     public type:string,
     public occupation:string,
     public homeinsurance:Homeinsurance,
     public properties:Property

    ){}
}
