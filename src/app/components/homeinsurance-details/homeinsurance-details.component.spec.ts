import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeinsuranceDetailsComponent } from './homeinsurance-details.component';

describe('HomeinsuranceDetailsComponent', () => {
  let component: HomeinsuranceDetailsComponent;
  let fixture: ComponentFixture<HomeinsuranceDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HomeinsuranceDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeinsuranceDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
