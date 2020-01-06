import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CinematicaTestModule } from '../../../test.module';
import { DataFaltaUpdateComponent } from 'app/entities/data-falta/data-falta-update.component';
import { DataFaltaService } from 'app/entities/data-falta/data-falta.service';
import { DataFalta } from 'app/shared/model/data-falta.model';

describe('Component Tests', () => {
  describe('DataFalta Management Update Component', () => {
    let comp: DataFaltaUpdateComponent;
    let fixture: ComponentFixture<DataFaltaUpdateComponent>;
    let service: DataFaltaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [DataFaltaUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DataFaltaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DataFaltaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DataFaltaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DataFalta(123);
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
        const entity = new DataFalta();
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
