import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CinematicaTestModule } from '../../../test.module';
import { FluxoCaixaUpdateComponent } from 'app/entities/fluxo-caixa/fluxo-caixa-update.component';
import { FluxoCaixaService } from 'app/entities/fluxo-caixa/fluxo-caixa.service';
import { FluxoCaixa } from 'app/shared/model/fluxo-caixa.model';

describe('Component Tests', () => {
  describe('FluxoCaixa Management Update Component', () => {
    let comp: FluxoCaixaUpdateComponent;
    let fixture: ComponentFixture<FluxoCaixaUpdateComponent>;
    let service: FluxoCaixaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [FluxoCaixaUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FluxoCaixaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FluxoCaixaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FluxoCaixaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FluxoCaixa(123);
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
        const entity = new FluxoCaixa();
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
