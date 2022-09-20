import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { HomeinsuranceComponent } from './components/homeinsurance/homeinsurance.component';
import { ContactusComponent } from './components/contactus/contactus.component';
import { PagenotfoundComponent } from './components/pagenotfound/pagenotfound.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { HomeinsuranceDetailsComponent } from './components/homeinsurance-details/homeinsurance-details.component';
import { SearchComponent } from './components/search/search.component';

import {HttpClientModule} from "@angular/common/http";
import { AdminModule } from './admin/admin.module';

import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    HomeinsuranceComponent,
    ContactusComponent,
    PagenotfoundComponent,
    NavbarComponent,
    HomeinsuranceDetailsComponent,
    SearchComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AdminModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
