import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddhomeinsuranceComponent } from './components/addhomeinsurance/addhomeinsurance.component';
import { AdminComponent } from './components/admin/admin.component';
import { DeleteinsuranceComponent } from './components/deleteinsurance/deleteinsurance.component';
import { UpdateinsuranceComponent } from './components/updateinsurance/updateinsurance.component';

const routes: Routes = [{path:'admin',component:AdminComponent,
children:[
 {path:'addInsurance',component:AddhomeinsuranceComponent},
 {path:'updateInsurance',component:UpdateinsuranceComponent},
 {path:'deleteInsurance',component:DeleteinsuranceComponent}
]}

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
