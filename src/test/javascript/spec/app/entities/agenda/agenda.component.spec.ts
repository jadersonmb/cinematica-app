import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CinematicaTestModule } from '../../../test.module';
import { AgendaComponent } from 'app/entities/agenda/agenda.component';
import { AgendaService } from 'app/entities/agenda/agenda.service';
import { Agenda } from 'app/shared/model/agenda.model';

describe('Component Tests', () => {
  describe('Agenda Management Component', () => {
    let comp: AgendaComponent;
    let fixture: ComponentFixture<AgendaComponent>;
    let service: AgendaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [AgendaComponent],
        providers: []
      })
        .overrideTemplate(AgendaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AgendaComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AgendaService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Agenda(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.agenda && comp.agenda[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
