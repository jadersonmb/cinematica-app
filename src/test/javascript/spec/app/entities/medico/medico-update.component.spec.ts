import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CinematicaTestModule } from '../../../test.module';
import { MedicoUpdateComponent } from 'app/entities/medico/medico-update.component';
import { MedicoService } from 'app/entities/medico/medico.service';
import { Medico } from 'app/shared/model/medico.model';

describe('Component Tests', () => {
  describe('Medico Management Update Component', () => {
    let comp: MedicoUpdateComponent;
    let fixture: ComponentFixture<MedicoUpdateComponent>;
    let service: MedicoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [MedicoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MedicoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MedicoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MedicoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Medico(123);
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
        const entity = new Medico();
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
