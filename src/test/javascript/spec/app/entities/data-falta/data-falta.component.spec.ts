import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CinematicaTestModule } from '../../../test.module';
import { DataFaltaComponent } from 'app/entities/data-falta/data-falta.component';
import { DataFaltaService } from 'app/entities/data-falta/data-falta.service';
import { DataFalta } from 'app/shared/model/data-falta.model';

describe('Component Tests', () => {
  describe('DataFalta Management Component', () => {
    let comp: DataFaltaComponent;
    let fixture: ComponentFixture<DataFaltaComponent>;
    let service: DataFaltaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [DataFaltaComponent],
        providers: []
      })
        .overrideTemplate(DataFaltaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DataFaltaComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DataFaltaService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new DataFalta(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.dataFaltas && comp.dataFaltas[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
