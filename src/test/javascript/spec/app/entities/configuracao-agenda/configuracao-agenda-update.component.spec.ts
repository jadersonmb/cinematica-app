import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CinematicaTestModule } from '../../../test.module';
import { ConfiguracaoAgendaUpdateComponent } from 'app/entities/configuracao-agenda/configuracao-agenda-update.component';
import { ConfiguracaoAgendaService } from 'app/entities/configuracao-agenda/configuracao-agenda.service';
import { ConfiguracaoAgenda } from 'app/shared/model/configuracao-agenda.model';

describe('Component Tests', () => {
  describe('ConfiguracaoAgenda Management Update Component', () => {
    let comp: ConfiguracaoAgendaUpdateComponent;
    let fixture: ComponentFixture<ConfiguracaoAgendaUpdateComponent>;
    let service: ConfiguracaoAgendaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [ConfiguracaoAgendaUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ConfiguracaoAgendaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConfiguracaoAgendaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ConfiguracaoAgendaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ConfiguracaoAgenda(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ConfiguracaoAgenda();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
