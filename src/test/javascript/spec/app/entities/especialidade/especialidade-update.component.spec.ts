import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CinematicaTestModule } from '../../../test.module';
import { EspecialidadeUpdateComponent } from 'app/entities/especialidade/especialidade-update.component';
import { EspecialidadeService } from 'app/entities/especialidade/especialidade.service';
import { Especialidade } from 'app/shared/model/especialidade.model';

describe('Component Tests', () => {
  describe('Especialidade Management Update Component', () => {
    let comp: EspecialidadeUpdateComponent;
    let fixture: ComponentFixture<EspecialidadeUpdateComponent>;
    let service: EspecialidadeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [EspecialidadeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(EspecialidadeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EspecialidadeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EspecialidadeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Especialidade(123);
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
        const entity = new Especialidade();
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
