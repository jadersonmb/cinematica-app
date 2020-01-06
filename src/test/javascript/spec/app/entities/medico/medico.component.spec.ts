import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CinematicaTestModule } from '../../../test.module';
import { MedicoComponent } from 'app/entities/medico/medico.component';
import { MedicoService } from 'app/entities/medico/medico.service';
import { Medico } from 'app/shared/model/medico.model';

describe('Component Tests', () => {
  describe('Medico Management Component', () => {
    let comp: MedicoComponent;
    let fixture: ComponentFixture<MedicoComponent>;
    let service: MedicoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [MedicoComponent],
        providers: []
      })
        .overrideTemplate(MedicoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MedicoComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MedicoService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Medico(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.medicos && comp.medicos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
