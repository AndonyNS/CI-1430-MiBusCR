require 'test_helper'

class RutaParadasControllerTest < ActionController::TestCase
  setup do
    @ruta_parada = ruta_paradas(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:ruta_paradas)
  end

  test "should create ruta_parada" do
    assert_difference('RutaParada.count') do
      post :create, ruta_parada: { parada_id: @ruta_parada.parada_id, ruta_id: @ruta_parada.ruta_id, tipo: @ruta_parada.tipo }
    end

    assert_response 201
  end

  test "should show ruta_parada" do
    get :show, id: @ruta_parada
    assert_response :success
  end

  test "should update ruta_parada" do
    put :update, id: @ruta_parada, ruta_parada: { parada_id: @ruta_parada.parada_id, ruta_id: @ruta_parada.ruta_id, tipo: @ruta_parada.tipo }
    assert_response 204
  end

  test "should destroy ruta_parada" do
    assert_difference('RutaParada.count', -1) do
      delete :destroy, id: @ruta_parada
    end

    assert_response 204
  end
end
