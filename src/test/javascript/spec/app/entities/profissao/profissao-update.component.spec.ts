import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CinematicaTestModule } from '../../../test.module';
import { ProfissaoUpdateComponent } from 'app/entities/profissao/profissao-update.component';
import { ProfissaoService } from 'app/entities/profissao/profissao.service';
import { Profissao } from 'app/shared/model/profissao.model';

describe('Component Tests', () => {
  describe('Profissao Management Update Component', () => {
    let comp: ProfissaoUpdateComponent;
    let fixture: ComponentFixture<ProfissaoUpdateComponent>;
    let service: ProfissaoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [ProfissaoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ProfissaoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProfissaoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProfissaoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Profissao(123);
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
        const entity = new Profissao();
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
