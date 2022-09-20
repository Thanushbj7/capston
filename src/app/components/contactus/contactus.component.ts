import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Customer } from 'src/app/models/customer';

@Component({
  selector: 'app-contactus',
  templateUrl: './contactus.component.html',
  styleUrls: ['./contactus.component.scss']
})
export class ContactusComponent implements OnInit {
  user:Customer={
    username:'',city:'',email:'',gender:''
  }
  constructor() { }

  ngOnInit(): void {
  }
  onRegister=(contactForm:NgForm)=>{
    console.log(contactForm.value);
    console.log(`${this.user.username}`);
  }

}
