import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CinematicaTestModule } from '../../../test.module';
import { DataFaltaDetailComponent } from 'app/entities/data-falta/data-falta-detail.component';
import { DataFalta } from 'app/shared/model/data-falta.model';

describe('Component Tests', () => {
  describe('DataFalta Management Detail Component', () => {
    let comp: DataFaltaDetailComponent;
    let fixture: ComponentFixture<DataFaltaDetailComponent>;
    const route = ({ data: of({ dataFalta: new DataFalta(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [DataFaltaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DataFaltaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DataFaltaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load dataFalta on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dataFalta).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
