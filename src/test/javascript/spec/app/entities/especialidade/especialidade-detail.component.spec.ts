import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CinematicaTestModule } from '../../../test.module';
import { EspecialidadeDetailComponent } from 'app/entities/especialidade/especialidade-detail.component';
import { Especialidade } from 'app/shared/model/especialidade.model';

describe('Component Tests', () => {
  describe('Especialidade Management Detail Component', () => {
    let comp: EspecialidadeDetailComponent;
    let fixture: ComponentFixture<EspecialidadeDetailComponent>;
    const route = ({ data: of({ especialidade: new Especialidade(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [EspecialidadeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(EspecialidadeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EspecialidadeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load especialidade on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.especialidade).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
