import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CinematicaTestModule } from '../../../test.module';
import { HorarioDisponivelUpdateComponent } from 'app/entities/horario-disponivel/horario-disponivel-update.component';
import { HorarioDisponivelService } from 'app/entities/horario-disponivel/horario-disponivel.service';
import { HorarioDisponivel } from 'app/shared/model/horario-disponivel.model';

describe('Component Tests', () => {
  describe('HorarioDisponivel Management Update Component', () => {
    let comp: HorarioDisponivelUpdateComponent;
    let fixture: ComponentFixture<HorarioDisponivelUpdateComponent>;
    let service: HorarioDisponivelService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [HorarioDisponivelUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(HorarioDisponivelUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HorarioDisponivelUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HorarioDisponivelService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new HorarioDisponivel(123);
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
        const entity = new HorarioDisponivel();
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
