import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CinematicaTestModule } from '../../../test.module';
import { HorarioUpdateComponent } from 'app/entities/horario/horario-update.component';
import { HorarioService } from 'app/entities/horario/horario.service';
import { Horario } from 'app/shared/model/horario.model';

describe('Component Tests', () => {
  describe('Horario Management Update Component', () => {
    let comp: HorarioUpdateComponent;
    let fixture: ComponentFixture<HorarioUpdateComponent>;
    let service: HorarioService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [HorarioUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(HorarioUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HorarioUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HorarioService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Horario(123);
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
        const entity = new Horario();
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
