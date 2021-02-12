import { APP_BASE_HREF } from '@angular/common';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { timer } from 'rxjs';
import { AppModule } from 'src/app/app.module';
import { Institution } from 'src/app/models/institution';
import { PublicFalsehood } from 'src/app/models/publicFalsehood';
import { PublicFigure } from 'src/app/models/publicFigure';
import { Region, RegionEntry } from 'src/app/models/region';
import { ApproveServiceService } from 'src/app/services/approve-service.service';
import { SearchService } from 'src/app/services/search.service';
import { SubmitService } from 'src/app/services/submit.service';
import { TokenService } from 'src/app/services/token.service';
import { environment } from 'src/environments/environment';
import { PublicFalsehoodSearchComponent } from '../public-falsehood-search/public-falsehood-search.component';

import { RegionComponent } from './region.component';

function delay(ms: number) {
  return new Promise( resolve => setTimeout(resolve, ms) );
}

describe('RegionComponent', () => {
  let component: RegionComponent;
  let fixture: ComponentFixture<RegionComponent>;
  let testController: HttpTestingController;

  let falsehoods :PublicFalsehood[] = [];
  let region1:Region;
  
  beforeAll(() => {

    let inst = new Institution();
    inst.approved = 1;
    inst.id = 1;
    inst.name = "Trec-Apps";
    region1 = new Region();
    region1.approved = 1;
    region1.id = 1;
    region1.name = "Trectopolis";
    let pubfig1 = new PublicFigure();
    pubfig1.approved = 1;
    pubfig1.id = 1;
    pubfig1.firstname = "Lord";
    pubfig1.lastName = "Tormontrec";
    let inst2 = new Institution();
    inst2.approved = 1;
    inst2.id = 2;
    inst2.name = "Tormonscript";
    let pubfig2 = new PublicFigure();
    pubfig2.approved = 1;
    pubfig2.id = 2;
    pubfig2.firstname = "Lord";
    pubfig2.lastName = "Trooper";


    falsehoods = [];

    let falsehood = new PublicFalsehood();
    falsehood.dateMade = new Date();
    falsehood.id = 3;
    falsehood.institution = inst;
    falsehood.official = pubfig1;
    falsehood.officialType = 0;
    falsehood.region = region1;
    falsehood.severity = 2;
    falsehoods.push(falsehood);

    falsehood = new PublicFalsehood();
    falsehood.dateMade = new Date();
    falsehood.id = 5;
    falsehood.institution = inst2;
    falsehood.official = pubfig2;
    falsehood.officialType = 0;
    falsehood.region = region1;
    falsehood.severity = 1;
    falsehoods.push(falsehood);
  });

  beforeEach((async() => {
    await TestBed.configureTestingModule({
      declarations:[
        RegionComponent,
        PublicFalsehoodSearchComponent
      ],
      imports: [
        HttpClientTestingModule,
        FormsModule,
        BrowserModule
      ],
      providers: [
        {provide: APP_BASE_HREF, useValue : '/' },
        TokenService,
        SearchService,
        SubmitService,
        ApproveServiceService,
        PublicFalsehoodSearchComponent
      ]
  })
  .compileComponents();

  fixture = TestBed.createComponent(RegionComponent);
  component = fixture.componentInstance;
  fixture.detectChanges();
  testController = TestBed.inject(HttpTestingController);
}));

  afterEach(() => {
    testController.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set createNew to true', () => {
    component.startCreateNew();
    expect(component.createNew).toBeTruthy();
  });

  it('Should set create new to false and create strings to empty strings!', () => {
    component.stopCreateNew();
    expect(component.createNew).toBeFalsy();
    expect(component.editContents).toEqual("");
    expect(component.editName).toEqual("");
  });

  it('should reset the new resources', () => {
    component.startCreateNew();
    expect(component.createNew).toBeTrue();

    component.stopCreateNew();
    expect(component.editContents).toBe("");
    expect(component.editName).toBe("");
    expect(component.createNew).toBeFalse();
  });

  it('should set the mode to 1', () => {
    component.setMode(1);
    expect(component.mode).toBe(1);
  });

  it('should add a new region!', async () => {
    component.editContents = "Content about the region!";
    component.editName = "some Region!";
    component.addNewReg();
    let req = testController.expectOne(environment.FALSEHOOD_URL + 
      "Update/PublicFalsehood/AddRegion", "Should have submitted a Region data");

    

    req.flush(true);

    await delay(100);
    
    expect(component.createNew).toBeFalsy();
    expect(component.editContents).toEqual("");
    expect(component.editName).toEqual("");
  });

  it('Should search for a given Region', async () => {

    component.mainRegion = new RegionEntry();
    component.mainRegion.contents = "Region contents!";
    component.mainRegion.region = new Region();
    component.mainRegion.region.approved = 1;
    component.mainRegion.region.id = 1;
    component.mainRegion.region.name = "Trectopolis";

    component.setMode(2);

    let req = testController.expectOne(environment.FALSEHOOD_URL +
      'PublicFalsehood/ByRegion/1?page=0&size=20', 'Should Have a call to search for falsehoods');
    
    req.flush(falsehoods);
    await delay(100);
    expect(component.searchComponent.falsehood).toBeNull();
    expect(component.searchComponent.falsehoods).toBe(falsehoods);
  });

  it('should search for the falsehoods with that region', async () => {

    let searchTerm = "Trec";
    component.onSearchUpdate({target: {value: searchTerm}});

    let req = testController.expectOne(environment.FALSEHOOD_URL + `PublicFalsehood/Regions/${searchTerm}`);

    req.flush([region1]);

    await delay(100);
    expect(component.searchRegion).toEqual([region1]);
  });

  it('should set up the main Region', async() => {
    let contents = 'Trec-Apps funded this Service!';

    let regEntry = new RegionEntry();
    regEntry.contents = contents;
    regEntry.region = region1;

    component.getRegion(1);
    let req = testController.expectOne(environment.FALSEHOOD_URL + "PublicFalsehood/Region/1");
    req.flush(regEntry);

    await delay(100);
    expect(component.mainRegion).toBe(regEntry);
  });

  it('should approve and reject a region', async ()=> {
    let tempRegion1 = new Region();
    tempRegion1.approved = 0;
    tempRegion1.id = 1;
    tempRegion1.name = "Trectopolis";

    let tempRegionEntry1 = new RegionEntry();
    tempRegionEntry1.contents = "Contents of 1";
    tempRegionEntry1.region = tempRegion1;

    let tempRegion2 = new Region();
    tempRegion2.approved = 0;
    tempRegion2.id = 2;
    tempRegion2.name = "Trectopolis";

    let tempRegionEntry2 = new RegionEntry();
    tempRegionEntry2.contents = "Contents of 2";
    tempRegionEntry2.region = tempRegion2;

    component.mainRegion = tempRegionEntry1;

    component.approveRegion(true);
    let req = testController.expectOne(environment.FALSEHOOD_URL + "Update/PublicFalsehood/ApproveRegion");
    req.flush(true);
    await delay(100);
    expect(component.mainRegion.region.approved).toEqual(1);

    component.mainRegion = tempRegionEntry2;
    component.approveRegion(false);
    req = testController.expectOne(environment.FALSEHOOD_URL + "Update/PublicFalsehood/RejectRegion");
    req.flush(true);
    await delay(100);
    expect(component.mainRegion.region.approved).toEqual(1);
  });

});
