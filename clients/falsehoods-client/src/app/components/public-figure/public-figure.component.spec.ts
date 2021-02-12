import { APP_BASE_HREF } from '@angular/common';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppModule } from 'src/app/app.module';
import { FalsehoodSearchComponent } from '../falsehood-search/falsehood-search.component';
import { PublicFalsehoodSearchComponent } from '../public-falsehood-search/public-falsehood-search.component';
import { ApproveServiceService } from 'src/app/services/approve-service.service';
import { SearchService } from 'src/app/services/search.service';
import { SubmitService } from 'src/app/services/submit.service';
import { TokenService } from 'src/app/services/token.service';
import { environment } from 'src/environments/environment';
import { PublicFigureComponent } from './public-figure.component';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { Falsehood } from 'src/app/models/falsehoods';
import { PublicFalsehood } from 'src/app/models/publicFalsehood';
import { PublicFigure, PublicFigureEntry } from 'src/app/models/publicFigure';
import { MediaOutlet } from 'src/app/models/mediaOutlet';
import { Region } from 'src/app/models/region';
import { Institution } from 'src/app/models/institution';

function delay(ms: number) {
  return new Promise( resolve => setTimeout(resolve, ms) );
}

describe('PublicFigureComponent', () => {
  let component: PublicFigureComponent;
  let fixture: ComponentFixture<PublicFigureComponent>;
  let testController: HttpTestingController;

  let mFalsehoods: Falsehood[] = [];
  let pFalsehoods: PublicFalsehood[] = [];

  let pubfig1: PublicFigure;
  let pubfig2: PublicFigure;

  beforeAll(()=> {
    pubfig1 = new PublicFigure();
    pubfig1.approved = 1;
    pubfig1.id = 1;
    pubfig1.firstname = "Lord";
    pubfig1.lastName = "Tormontrec";

    pubfig2 = new PublicFigure();
    pubfig2.approved = 1;
    pubfig2.id = 2;
    pubfig2.firstname = "Lord";
    pubfig2.middleNames = 'Johnathan';
    pubfig2.lastName = "Tormontrec";


    let outlet = new MediaOutlet();
    outlet.approved = 1;
    outlet.foundationYear = 2022;
    outlet.name = "Trec-News";
    outlet.outletId = 1;

    let region1 = new Region();
    region1.approved = 1;
    region1.id = 1;
    region1.name = "Trectopolis";

    let region2 = new Region();
    region2.approved = 1;
    region2.id = 2;
    region2.name = "Trectopia";

    let inst = new Institution();
    inst.approved = 1;
    inst.id = 1;
    inst.name = "Trec-Apps";

    let inst2 = new Institution();
    inst2.approved = 1;
    inst2.id = 2;
    inst2.name = "Tormonscript";

    mFalsehoods = [];

    let falsehood = new Falsehood();
    falsehood.author1 = pubfig1;
    falsehood.contentId = "Article1";
    falsehood.dateMade = new Date();
    falsehood.id = 2;
    falsehood.outlet = outlet;
    falsehood.severity = 3;
    mFalsehoods.push(falsehood);

    falsehood = new Falsehood();
    falsehood.author1 = pubfig1;
    falsehood.contentId = "Article2";
    falsehood.dateMade = new Date();
    falsehood.id = 4;
    falsehood.outlet = outlet;
    falsehood.severity = 4;
    mFalsehoods.push(falsehood);

    pFalsehoods = [];

    let pfalsehood = new PublicFalsehood();
    pfalsehood.dateMade = new Date();
    pfalsehood.id = 3;
    pfalsehood.institution = inst;
    pfalsehood.official = pubfig1;
    pfalsehood.officialType = 0;
    pfalsehood.region = region1;
    pfalsehood.severity = 2;
    pFalsehoods.push(pfalsehood);

    pfalsehood = new PublicFalsehood();
    pfalsehood.dateMade = new Date();
    pfalsehood.id = 5;
    pfalsehood.institution = inst2;
    pfalsehood.official = pubfig1;
    pfalsehood.officialType = 0;
    pfalsehood.region = region2;
    pfalsehood.severity = 1;
    pFalsehoods.push(pfalsehood);
  });

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        PublicFigureComponent,
        PublicFalsehoodSearchComponent,
        FalsehoodSearchComponent
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
        ApproveServiceService
      ]
  })
  .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PublicFigureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    testController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    testController.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should reset the new resources', () => {
    component.startCreateNew();
    expect(component.createNew).toBeTrue();

    component.stopCreateNew();
    expect(component.editContents).toBe("");
    expect(component.editName).toBe("");
    expect(component.createNew).toBeFalse();
  });

  it('should add a new Public Figure!', async () => {
    component.editContents = "Content about the Figure!";
    component.editName = "some Figure!";
    component.addNewFig();
    let req = testController.expectOne(environment.FALSEHOOD_URL + 
      "PublicFigure/Add", "Should have submitted Public figure data 1");

    req.flush(true);

    await delay(100);

    component.editContents = "Content about the Figure 2!";
    component.editName = "some Public Figure!";
    component.addNewFig();
    req = testController.expectOne(environment.FALSEHOOD_URL + 
      "PublicFigure/Add", "Should have submitted Public figure data 2");

    req.flush(true);

    await delay(100);
    
    expect(component.createNew).toBeFalsy();
    expect(component.editContents).toEqual("");
    expect(component.editName).toEqual("");
  });

  it('Should search for a given institution', async () => {

    component.mainFigure = new PublicFigureEntry();
    component.mainFigure.text = "Public Figure contents!";
    component.mainFigure.figure = new PublicFigure();
    component.mainFigure.figure.approved = 1;
    component.mainFigure.figure.id = 1;
    component.mainFigure.figure.firstname = "Lord";
    component.mainFigure.figure.lastName = "Tormontrec";

    component.setMode(2);

    let req = testController.expectOne(environment.FALSEHOOD_URL +
      'PublicFalsehood/searchConfirmed', 'Should Have a call to search for Public falsehoods');
    
    req.flush(pFalsehoods);
    await delay(100);
    expect(component.publicSearchComponent.falsehood).toBeNull();
    expect(component.publicSearchComponent.falsehoods).toBe(pFalsehoods);

    component.setMode(3);

    req = testController.expectOne(environment.FALSEHOOD_URL +
      'Falsehood/searchConfirmed', 'Should Have a call to search for falsehoods');
    
    req.flush(mFalsehoods);
    await delay(100);
    expect(component.searchComponent.falsehood).toBeNull();
    expect(component.searchComponent.falsehoods).toBe(mFalsehoods);
  });

  it('should search for the falsehoods with that institution', async () => {

    let searchTerm = "Trec";
    component.onSearchUpdate({target: {value: searchTerm}});

    let req = testController.expectOne(environment.FALSEHOOD_URL + `PublicFigure/listByName/${searchTerm}`);

    req.flush([pubfig1]);

    await delay(100);
    expect(component.searchFigures).toEqual([pubfig1]);
  });

  it('should set up the main Institution', async() => {
    let contents = 'Tormontrec created this Service!';

    let figEntry = new PublicFigureEntry();
    figEntry.text = contents;
    figEntry.figure = pubfig1;

    component.getFigure(1);
    let req = testController.expectOne(environment.FALSEHOOD_URL + "PublicFigure/id/1");
    req.flush(figEntry);

    await delay(100);
    expect(component.mainFigure).toEqual(figEntry);
  });

  it('should approve and reject a public figure', async ()=> {
    let tempFigure1 = new PublicFigure();
    tempFigure1.approved = 0;
    tempFigure1.id = 1;
    tempFigure1.firstname = "Lord";
    tempFigure1.lastName = "Tormontrec";

    let tempFigureEntry1 = new PublicFigureEntry();
    tempFigureEntry1.text = "Contents of 1";
    tempFigureEntry1.figure = tempFigure1;

    let tempFigure2 = new PublicFigure();
    tempFigure2.approved = 0;
    tempFigure2.id = 1;
    tempFigure2.firstname = "Lord";
    tempFigure2.lastName = "Tormontrec";

    let tempFigureEntry2 = new PublicFigureEntry();
    tempFigureEntry2.text = "Contents of 2";
    tempFigureEntry2.figure = tempFigure2;

    component.mainFigure = tempFigureEntry1;

    component.approveFigure(true);
    let req = testController.expectOne(environment.FALSEHOOD_URL + "PublicFigure/Approve");
    req.flush(true);
    await delay(100);
    expect(component.mainFigure.figure.approved).toEqual(1);

    component.mainFigure = tempFigureEntry2;
    component.approveFigure(false);
    req = testController.expectOne(environment.FALSEHOOD_URL + "PublicFigure/Reject");
    req.flush(true);
    await delay(100);
    expect(component.mainFigure.figure.approved).toEqual(1);
  });
});
