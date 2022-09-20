import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';

import { AdminComponent } from './components/admin/admin.component';
import { AddhomeinsuranceComponent } from './components/addhomeinsurance/addhomeinsurance.component';
import { UpdateinsuranceComponent } from './components/updateinsurance/updateinsurance.component';
import { DeleteinsuranceComponent } from './components/deleteinsurance/deleteinsurance.component';
import { FormsModule } from '@angular/forms';


@NgModule({
  declarations: [
  
    AdminComponent,
    AddhomeinsuranceComponent,
    UpdateinsuranceComponent,
    DeleteinsuranceComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    AdminRoutingModule
  ]
})
export class AdminModule { }
