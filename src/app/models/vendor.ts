import { Address } from "./address";

export class Vendor {
    constructor(

        public vendorId:number,
        public name:string,
        public address:Address
    ){

    }
}
