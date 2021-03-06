require 'rails_helper'

# This spec was generated by rspec-rails when you ran the scaffold generator.
# It demonstrates how one might use RSpec to specify the controller code that
# was generated by Rails when you ran the scaffold generator.
#
# It assumes that the implementation code is generated by the rails scaffold
# generator.  If you are using any extension libraries to generate different
# controller code, this generated spec may or may not pass.
#
# It only uses APIs available in rails and/or rspec-rails.  There are a number
# of tools you can use to make these specs even more expressive, but we're
# sticking to rails and rspec-rails APIs to keep things simple and stable.
#
# Compared to earlier versions of this generator, there is very limited use of
# stubs and message expectations in this spec.  Stubs are only used when there
# is no simpler way to get a handle on the object needed for the example.
# Message expectations are only used when there is no simpler way to specify
# that an instance is receiving a specific message.

RSpec.describe RutaUsuariosController, :type => :controller do

  # This should return the minimal set of attributes required to create a valid
  # RutaUsuario. As you add validations to RutaUsuario, be sure to
  # adjust the attributes here as well.
  let(:valid_attributes) {
    skip("Add a hash of attributes valid for your model")
  }

  let(:invalid_attributes) {
    skip("Add a hash of attributes invalid for your model")
  }

  # This should return the minimal set of values that should be in the session
  # in order to pass any filters (e.g. authentication) defined in
  # RutaUsuariosController. Be sure to keep this updated too.
  let(:valid_session) { {} }

  describe "GET index" do
    it "assigns all ruta_usuarios as @ruta_usuarios" do
      ruta_usuario = RutaUsuario.create! valid_attributes
      get :index, {}, valid_session
      expect(assigns(:ruta_usuarios)).to eq([ruta_usuario])
    end
  end

  describe "GET show" do
    it "assigns the requested ruta_usuario as @ruta_usuario" do
      ruta_usuario = RutaUsuario.create! valid_attributes
      get :show, {:id => ruta_usuario.to_param}, valid_session
      expect(assigns(:ruta_usuario)).to eq(ruta_usuario)
    end
  end

  describe "GET new" do
    it "assigns a new ruta_usuario as @ruta_usuario" do
      get :new, {}, valid_session
      expect(assigns(:ruta_usuario)).to be_a_new(RutaUsuario)
    end
  end

  describe "GET edit" do
    it "assigns the requested ruta_usuario as @ruta_usuario" do
      ruta_usuario = RutaUsuario.create! valid_attributes
      get :edit, {:id => ruta_usuario.to_param}, valid_session
      expect(assigns(:ruta_usuario)).to eq(ruta_usuario)
    end
  end

  describe "POST create" do
    describe "with valid params" do
      it "creates a new RutaUsuario" do
        expect {
          post :create, {:ruta_usuario => valid_attributes}, valid_session
        }.to change(RutaUsuario, :count).by(1)
      end

      it "assigns a newly created ruta_usuario as @ruta_usuario" do
        post :create, {:ruta_usuario => valid_attributes}, valid_session
        expect(assigns(:ruta_usuario)).to be_a(RutaUsuario)
        expect(assigns(:ruta_usuario)).to be_persisted
      end

      it "redirects to the created ruta_usuario" do
        post :create, {:ruta_usuario => valid_attributes}, valid_session
        expect(response).to redirect_to(RutaUsuario.last)
      end
    end

    describe "with invalid params" do
      it "assigns a newly created but unsaved ruta_usuario as @ruta_usuario" do
        post :create, {:ruta_usuario => invalid_attributes}, valid_session
        expect(assigns(:ruta_usuario)).to be_a_new(RutaUsuario)
      end

      it "re-renders the 'new' template" do
        post :create, {:ruta_usuario => invalid_attributes}, valid_session
        expect(response).to render_template("new")
      end
    end
  end

  describe "PUT update" do
    describe "with valid params" do
      let(:new_attributes) {
        skip("Add a hash of attributes valid for your model")
      }

      it "updates the requested ruta_usuario" do
        ruta_usuario = RutaUsuario.create! valid_attributes
        put :update, {:id => ruta_usuario.to_param, :ruta_usuario => new_attributes}, valid_session
        ruta_usuario.reload
        skip("Add assertions for updated state")
      end

      it "assigns the requested ruta_usuario as @ruta_usuario" do
        ruta_usuario = RutaUsuario.create! valid_attributes
        put :update, {:id => ruta_usuario.to_param, :ruta_usuario => valid_attributes}, valid_session
        expect(assigns(:ruta_usuario)).to eq(ruta_usuario)
      end

      it "redirects to the ruta_usuario" do
        ruta_usuario = RutaUsuario.create! valid_attributes
        put :update, {:id => ruta_usuario.to_param, :ruta_usuario => valid_attributes}, valid_session
        expect(response).to redirect_to(ruta_usuario)
      end
    end

    describe "with invalid params" do
      it "assigns the ruta_usuario as @ruta_usuario" do
        ruta_usuario = RutaUsuario.create! valid_attributes
        put :update, {:id => ruta_usuario.to_param, :ruta_usuario => invalid_attributes}, valid_session
        expect(assigns(:ruta_usuario)).to eq(ruta_usuario)
      end

      it "re-renders the 'edit' template" do
        ruta_usuario = RutaUsuario.create! valid_attributes
        put :update, {:id => ruta_usuario.to_param, :ruta_usuario => invalid_attributes}, valid_session
        expect(response).to render_template("edit")
      end
    end
  end

  describe "DELETE destroy" do
    it "destroys the requested ruta_usuario" do
      ruta_usuario = RutaUsuario.create! valid_attributes
      expect {
        delete :destroy, {:id => ruta_usuario.to_param}, valid_session
      }.to change(RutaUsuario, :count).by(-1)
    end

    it "redirects to the ruta_usuarios list" do
      ruta_usuario = RutaUsuario.create! valid_attributes
      delete :destroy, {:id => ruta_usuario.to_param}, valid_session
      expect(response).to redirect_to(ruta_usuarios_url)
    end
  end

end
