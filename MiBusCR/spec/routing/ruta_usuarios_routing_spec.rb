require "rails_helper"

RSpec.describe RutaUsuariosController, :type => :routing do
  describe "routing" do

    it "routes to #index" do
      expect(:get => "/ruta_usuarios").to route_to("ruta_usuarios#index")
    end

    it "routes to #new" do
      expect(:get => "/ruta_usuarios/new").to route_to("ruta_usuarios#new")
    end

    it "routes to #show" do
      expect(:get => "/ruta_usuarios/1").to route_to("ruta_usuarios#show", :id => "1")
    end

    it "routes to #edit" do
      expect(:get => "/ruta_usuarios/1/edit").to route_to("ruta_usuarios#edit", :id => "1")
    end

    it "routes to #create" do
      expect(:post => "/ruta_usuarios").to route_to("ruta_usuarios#create")
    end

    it "routes to #update" do
      expect(:put => "/ruta_usuarios/1").to route_to("ruta_usuarios#update", :id => "1")
    end

    it "routes to #destroy" do
      expect(:delete => "/ruta_usuarios/1").to route_to("ruta_usuarios#destroy", :id => "1")
    end

  end
end
