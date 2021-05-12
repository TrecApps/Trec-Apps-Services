import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FalsehoodsComponent } from './components/falsehoods/falsehoods.component';
import { InstitutionComponent } from './components/institution/institution.component';
import { MediaOutletComponent } from './components/media-outlet/media-outlet.component';
import { PublicFigureComponent } from './components/public-figure/public-figure.component';
import { RegionComponent } from './components/region/region.component';
import { WelcomeComponent } from './components/welcome/welcome.component';


const routes: Routes = [
	{ path: 'Welcome', component: WelcomeComponent},
  { path: 'Falsehoods', component: FalsehoodsComponent },
  { path: 'PublicFalsehoods', component: PublicFigureComponent },
  { path: 'Regions', component: RegionComponent },
  { path: 'Institution', component: InstitutionComponent},
  { path: 'PublicFigure', component: PublicFigureComponent},
  { path: 'MediaOutlet', component: MediaOutletComponent},
  { path: '',   redirectTo: '/Welcome', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
