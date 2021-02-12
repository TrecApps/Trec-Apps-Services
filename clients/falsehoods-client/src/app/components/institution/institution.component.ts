import { Component, OnInit, ViewChild } from '@angular/core';
import { Institution, InstitutionEntry } from 'src/app/models/institution';
import { ResourceSearchService } from 'src/app/services/resource-search.service';

@Component({
  selector: 'app-institution',
  templateUrl: './institution.component.html',
  styleUrls: ['./institution.component.css']
})
export class InstitutionComponent implements OnInit {

  mainInst: InstitutionEntry;

  mode: Number;

  createNew: boolean;

  editName: String;
  editContents: String;
  // @ViewChild(PublicFalsehoodSearchComponent) searchComponent: PublicFalsehoodSearchComponent;

  searchInst: Institution[];
  searchText: String;

  constructor(private resourceSearch: ResourceSearchService) {
    this.mode = 0;
    this.createNew = false;

    this.editContents = "";
    this.mainInst = new InstitutionEntry();


    this.searchInst = [];
    this.searchText = "";

    // this.searchComponent = new PublicFalsehoodSearchComponent(search);
   }

  ngOnInit() {
  }

  setMode(m: Number) {
    this.mode = m;

    if(this.mode == 2 && this.mainInst) {
      // this.searchComponent.initializeListByInstitution(this.mainInst.institution.id);
    }

  }


  async onSearchUpdate(event:any){
    let p = this.resourceSearch.searchInstitutions(event.target.value)
    p.then((inst: Institution[])=> {
      this.searchInst = inst;
    });
  }

  async getInst(id: Number) {
    let p = this.resourceSearch.getInstitution(id);
    p.then((inst: InstitutionEntry)=> {
      this.mainInst = inst;
    });

    this.searchText = "";
  }


}
