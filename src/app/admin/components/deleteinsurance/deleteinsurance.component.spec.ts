import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteinsuranceComponent } from './deleteinsurance.component';

describe('DeleteinsuranceComponent', () => {
  let component: DeleteinsuranceComponent;
  let fixture: ComponentFixture<DeleteinsuranceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeleteinsuranceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteinsuranceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
