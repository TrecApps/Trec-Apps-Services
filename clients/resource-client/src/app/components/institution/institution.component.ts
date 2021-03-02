import { Component, OnInit } from '@angular/core';
import { Institution, InstitutionEntry } from 'src/app/models/institution';
import { SearchService } from 'src/app/services/search.service';
import { UpdateService } from 'src/app/services/update.service';

@Component({
  selector: 'app-institution',
  templateUrl: './institution.component.html',
  styleUrls: ['./institution.component.css']
})
export class InstitutionComponent implements OnInit {

  mainInstitution: InstitutionEntry;

  searchInstitution: Institution[];
  editMode: boolean;
  searchText:String;
  constructor(private search: SearchService, private update: UpdateService) {
    this.mainInstitution = null;
    this.searchInstitution = [];
    this.editMode = false;
    this.searchText = "";
   }

  ngOnInit(): void {
  }

  updateReg() {
    if(!this.mainInstitution){
      return;
    }

    this.update.updateInstitution(this.mainInstitution);
    this.mainInstitution = null;
  }

  async onSearchUpdate(event:any){
    let p = this.search.searchInstitutions(event.target.value)
    p.then((institutions: Institution[])=> {
      this.searchInstitution = institutions;
    });
  }

  initializeNew() {
    let reg = new InstitutionEntry();
    reg.contents = "";
    reg.institution = new Institution();
    reg.institution.id = null;
    this.editMode = false;
    
    this.mainInstitution = reg;
  }

  async getInstitution(id: Number) {
    let p = this.search.getInstitution(id);
    p.then((institution: InstitutionEntry)=> {
      this.mainInstitution = institution;
      this.editMode = false;
    });

    this.searchText = "";
  }

  edit() {
    this.editMode = true;
  }
}
