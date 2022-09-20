import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddhomeinsuranceComponent } from './addhomeinsurance.component';

describe('AddhomeinsuranceComponent', () => {
  let component: AddhomeinsuranceComponent;
  let fixture: ComponentFixture<AddhomeinsuranceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddhomeinsuranceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddhomeinsuranceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
