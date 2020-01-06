import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CinematicaTestModule } from '../../../test.module';
import { PacienteComponent } from 'app/entities/paciente/paciente.component';
import { PacienteService } from 'app/entities/paciente/paciente.service';
import { Paciente } from 'app/shared/model/paciente.model';

describe('Component Tests', () => {
  describe('Paciente Management Component', () => {
    let comp: PacienteComponent;
    let fixture: ComponentFixture<PacienteComponent>;
    let service: PacienteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [PacienteComponent],
        providers: []
      })
        .overrideTemplate(PacienteComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PacienteComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PacienteService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Paciente(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.pacientes && comp.pacientes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
