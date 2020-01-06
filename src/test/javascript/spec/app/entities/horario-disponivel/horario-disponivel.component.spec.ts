import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CinematicaTestModule } from '../../../test.module';
import { HorarioDisponivelComponent } from 'app/entities/horario-disponivel/horario-disponivel.component';
import { HorarioDisponivelService } from 'app/entities/horario-disponivel/horario-disponivel.service';
import { HorarioDisponivel } from 'app/shared/model/horario-disponivel.model';

describe('Component Tests', () => {
  describe('HorarioDisponivel Management Component', () => {
    let comp: HorarioDisponivelComponent;
    let fixture: ComponentFixture<HorarioDisponivelComponent>;
    let service: HorarioDisponivelService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [HorarioDisponivelComponent],
        providers: []
      })
        .overrideTemplate(HorarioDisponivelComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HorarioDisponivelComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HorarioDisponivelService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new HorarioDisponivel(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.horarioDisponivels && comp.horarioDisponivels[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
