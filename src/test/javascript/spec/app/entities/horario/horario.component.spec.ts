import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CinematicaTestModule } from '../../../test.module';
import { HorarioComponent } from 'app/entities/horario/horario.component';
import { HorarioService } from 'app/entities/horario/horario.service';
import { Horario } from 'app/shared/model/horario.model';

describe('Component Tests', () => {
  describe('Horario Management Component', () => {
    let comp: HorarioComponent;
    let fixture: ComponentFixture<HorarioComponent>;
    let service: HorarioService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [HorarioComponent],
        providers: []
      })
        .overrideTemplate(HorarioComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HorarioComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HorarioService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Horario(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.horarios && comp.horarios[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
