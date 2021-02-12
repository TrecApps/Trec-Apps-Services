import { Component, OnInit, ViewChild, ViewChildren } from '@angular/core';
import { ResourceSearchService } from 'src/app/services/resource-search.service';
import {Region, RegionEntry } from '../../models/region';


@Component({
  selector: 'app-region',
  templateUrl: './region.component.html',
  styleUrls: ['./region.component.css']
})
export class RegionComponent implements OnInit {

  mainRegion: RegionEntry;

  mode: Number;

  createNew: boolean;

  editName: String;
  editContents: String;


  searchText: String;

  searchRegion: Region[];

  // @ViewChild(PublicFalsehoodSearchComponent) searchComponent: PublicFalsehoodSearchComponent;
  constructor(private resourceSearch: ResourceSearchService) {
    this.mode = 0;
    this.createNew = false;

    this.editContents = this.editName = "";
    this.mainRegion = new RegionEntry();

    this.searchText = "";
    this.searchRegion = [];
    // this.searchComponent = searchComponent;
   }

  ngOnInit() {
  }


  setMode(m: Number) {
    this.mode = m;

    if(this.mode == 2 && this.mainRegion) {
      // this.resourceSearch.initializeListByRegion(this.mainRegion.region.id);
    }

  }

  startCreateNew() {
    this.createNew = true;
  }

  stopCreateNew() {
    this.editContents = "";
    this.editName = "";
    this.createNew=false;
  }

  async onSearchUpdate(event:any){
    let p = this.resourceSearch.searchRegions(event.target.value)
    p.then((regions: Region[])=> {
      this.searchRegion = regions;
    });
  }

  async getRegion(id: Number) {
    let p = this.resourceSearch.getRegion(id);
    p.then((region: RegionEntry)=> {
      this.mainRegion = region;
    });

    this.searchText = "";
  }


}
