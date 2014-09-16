require 'test_helper'

class ParadasControllerTest < ActionController::TestCase
  setup do
    @parada = paradas(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:paradas)
  end

  test "should create parada" do
    assert_difference('Parada.count') do
      post :create, parada: { latitud: @parada.latitud, longitud: @parada.longitud, nombre: @parada.nombre, techo: @parada.techo }
    end

    assert_response 201
  end

  test "should show parada" do
    get :show, id: @parada
    assert_response :success
  end

  test "should update parada" do
    put :update, id: @parada, parada: { latitud: @parada.latitud, longitud: @parada.longitud, nombre: @parada.nombre, techo: @parada.techo }
    assert_response 204
  end

  test "should destroy parada" do
    assert_difference('Parada.count', -1) do
      delete :destroy, id: @parada
    end

    assert_response 204
  end
end
