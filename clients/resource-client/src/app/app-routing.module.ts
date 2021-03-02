import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { WelcomeComponent } from './components/welcome/welcome.component';
import { RegionComponent } from './components/region/region.component';
import { InstitutionComponent } from './components/institution/institution.component';
import { FigureComponent } from './components/figure/figure.component';
import { OutletComponent } from './components/outlet/outlet.component';


const routes: Routes = [
  { path: 'Welcome', component: WelcomeComponent},
  { path: 'Regions', component: RegionComponent },
  { path: 'Institution', component: InstitutionComponent},
  { path: 'PublicFigure', component: FigureComponent},
  { path: 'MediaOutlet', component: OutletComponent},
  { path: '',   redirectTo: '/Welcome', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
