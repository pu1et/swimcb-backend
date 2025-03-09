package com.project.swimcb.bo.swimmingclass.adapter.out;

import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassTypesResponse.ClassSubType;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassTypesResponse.ClassType;
import com.project.swimcb.bo.swimmingclass.adapter.out.FindBoSwimmingClassTypeDataMapper.SwimmingClassTypeWithSubType;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindBoSwimmingClassTypeDataMapperTest {

  private FindBoSwimmingClassTypeDataMapper mapper;

  @Mock
  private EntityManager entityManager;

  @Mock
  private EntityManagerFactory entityManagerFactory;

  @Mock
  private Query query;

  @BeforeEach
  void setUp() {
    when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
    when(entityManager.createQuery(anyString())).thenReturn(query);

    mapper = spy(new FindBoSwimmingClassTypeDataMapper(entityManager));
  }

  @Test
  @DisplayName("조회한 강습형태/강습구분을 응답형태에 맞게 정상적으로 가공하여 반환한다.")
  void shouldMapSwimmingClassTypeAndClassTypeToResponse() {
    // given
    val swimmingPoolId = 1L;
    val result = TestSwimmingClassTypeWithSubTypeFactory.create();

    when(mapper.findClassTypeWithSubTypes(anyLong())).thenReturn(result);
    // when
    val response = mapper.findBoSwimmingClassTypes(swimmingPoolId);
    // then
    assertThat(response.classTypes()).hasSize(2);

    // typeId, subTypeId 순서대로 정렬되는지 확인
    assertThat(response.classTypes()).extracting(ClassType::classTypeId)
        .containsExactly(1L, 2L);
    assertThat(response.classTypes().get(0).classSubTypes())
        .extracting(ClassSubType::classSubTypeId)
        .containsExactly(1L, 2L);
    assertThat(response.classTypes().get(1).classSubTypes())
        .extracting(ClassSubType::classSubTypeId)
        .containsExactly(3L, 4L);
  }

  @Test
  @DisplayName("강습형태에 속한 강습구분이 없으면 빈 리스트를 반환한다.")
  void shouldReturnEmptyListWhenClassSubTypeIsEmpty() {
    // given
    val swimmingPoolId = 1L;
    val result = TestSwimmingClassTypeWithSubTypeFactory.classTypeWithNoSubType();

    when(mapper.findClassTypeWithSubTypes(anyLong())).thenReturn(result);
    // when
    val response = mapper.findBoSwimmingClassTypes(swimmingPoolId);
    // then
    assertThat(response.classTypes()).hasSize(2);
    assertThat(response.classTypes()).extracting(ClassType::classTypeId)
        .containsExactly(1L, 2L);
    assertThat(response.classTypes().get(0).classSubTypes()).isEmpty();
    assertThat(response.classTypes().get(1).classSubTypes()).isEmpty();
  }

  private static class TestSwimmingClassTypeWithSubTypeFactory {

    private static List<SwimmingClassTypeWithSubType> create() {
      return List.of(
          SwimmingClassTypeWithSubType.builder()
              .swimmingClassTypeId(1L)
              .swimmingClassTypeName(GROUP)
              .swimmingClassSubTypeId(1L)
              .swimmingClassSubTypeName("DUMMY_SUB_TYPE_NAME1")
              .build(),
          SwimmingClassTypeWithSubType.builder()
              .swimmingClassTypeId(1L)
              .swimmingClassTypeName(GROUP)
              .swimmingClassSubTypeId(2L)
              .swimmingClassSubTypeName("DUMMY_SUB_TYPE_NAME2")
              .build(),
          SwimmingClassTypeWithSubType.builder()
              .swimmingClassTypeId(2L)
              .swimmingClassTypeName(KIDS_SWIMMING)
              .swimmingClassSubTypeId(3L)
              .swimmingClassSubTypeName("DUMMY_SUB_TYPE_NAME3")
              .build(),
          SwimmingClassTypeWithSubType.builder()
              .swimmingClassTypeId(2L)
              .swimmingClassTypeName(KIDS_SWIMMING)
              .swimmingClassSubTypeId(4L)
              .swimmingClassSubTypeName("DUMMY_SUB_TYPE_NAME4")
              .build()
      );
    }

    private static List<SwimmingClassTypeWithSubType> classTypeWithNoSubType() {
      return List.of(
          SwimmingClassTypeWithSubType.builder()
              .swimmingClassTypeId(1L)
              .swimmingClassTypeName(GROUP)
              .build(),
          SwimmingClassTypeWithSubType.builder()
              .swimmingClassTypeId(2L)
              .swimmingClassTypeName(GROUP)
              .build()
      );
    }
  }
}